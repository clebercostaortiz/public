package br.com.comprasa2.apicore.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.comprasa2.apicore.exception.DuplicatedException;
import br.com.comprasa2.apicore.exception.HashErrorException;
import br.com.comprasa2.apicore.model.ShortInfoUser;
import br.com.comprasa2.apicore.model.Usuario;
import br.com.comprasa2.apicore.repository.UserRepository;

@Service
@Component
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public Usuario usuarioLogado = null;



	public UserService() {
	}



	public Usuario checkUserByEmailOrPhone(String userOrPhone) {
		Usuario user = null;
		if(userOrPhone.contains("@")) {
			user =  userRepository.findOneByUserName(userOrPhone);
		}
		else {
			user =  userRepository.findOneByPhone(userOrPhone);
		}
		if(user != null) {
			user.setPassword(null);
		}
		return user;

	}

	public Usuario loadUserByUsername(String userName) {
		Usuario user =  userRepository.findOneByUserName(userName);
		if(user != null) {
			user.setPassword(null);
		}		
		return user;

	}

	public Usuario loadUserById(String id) {
		Optional<Usuario> user =  userRepository.findById(id);
		if(user.isPresent()) {
			user.get().setPassword(null);
			return user.get();
		}		
		return null;
	}

	public Usuario loadUserByUsernameAndPass(String userName, String clearPassword) throws HashErrorException {

		String hashPassword="";

		try {
			hashPassword = this.hashPassword(clearPassword);
		} catch (NoSuchAlgorithmException e) {
			throw new HashErrorException(e.getMessage());
		} catch (InvalidKeySpecException e) {
			throw new HashErrorException(e.getMessage());
		}

		Usuario user = null;
		if(userName.contains("@")) {
			user = userRepository.findOneByUserNameAndPassword(userName, hashPassword);
		}
		else {
			user = userRepository.findOneByPhoneAndPassword(userName, hashPassword);
		}

		if(user != null) {
			user.setPassword(null);
		}		
		return user;

	}


	public List<ShortInfoUser> addUserToUser(String userEmailOrPhone, String userId) throws DuplicatedException{

		Usuario userFoundForAttach = checkUserByEmailOrPhone(userEmailOrPhone);
		if(userFoundForAttach == null) {
			throw new NotFoundException("Usuário não encontrado!");
		}

		Usuario usuarioTrabalho = loadUserById(userId);
		if(usuarioTrabalho.getUsers().stream().anyMatch(r-> 
		r.getEmail().toLowerCase().equals(userEmailOrPhone.toLowerCase()) ||
		r.getPhone().toLowerCase().equals(userEmailOrPhone.toLowerCase()))) {
			throw new DuplicatedException("Usuário já está na lista!");
		}

		mongoTemplate.updateFirst(
				Query.query(Criteria.where("_id").is(userId)), 
				new Update().push("users", new ShortInfoUser(userFoundForAttach)), Usuario.class);

		usuarioTrabalho.getUsers().add(new ShortInfoUser(userFoundForAttach));
		return usuarioTrabalho.getUsers();
	}
	public List<ShortInfoUser> removeUserFromUser(String userEmailOrPhone, String userId){

		Usuario usuarioTrabalho = loadUserById(userId);
		Optional<ShortInfoUser> userFound = usuarioTrabalho.getUsers().stream().filter(r-> 
		r.getEmail().toLowerCase().equals(userEmailOrPhone.toLowerCase()) ||
		r.getPhone().toLowerCase().equals(userEmailOrPhone.toLowerCase())).findFirst();

		if(!userFound.isPresent()) {
			throw new NotFoundException("Usuário não encontrado!");
		}


		mongoTemplate.updateFirst(
				Query.query(Criteria.where("_id").is(userId)), 
				new Update().pull("users", userFound), Usuario.class);

		Usuario userSaida = loadUserById(userId);
		return userSaida.getUsers();
	}
	public List<ShortInfoUser> loadUsersFromUser(String id) {
		Usuario user =  loadUserById(id);
		return user.getUsers();
	}



	public Usuario insert(Usuario model) throws HashErrorException, DuplicatedException {

		Usuario userFound = checkUserByEmailOrPhone(model.getUsername());
		if(userFound!= null) {
			throw new DuplicatedException("Já existe um usuário com este e-mail");
		}

		userFound = checkUserByEmailOrPhone(model.getPhone());
		if(userFound!= null) {
			throw new DuplicatedException("Já existe um usuário com este telefone");
		}		


		model.setId(null);
		String hashPassword="";

		try {
			hashPassword = this.hashPassword(model.getPassword());
		} catch (NoSuchAlgorithmException e) {
			throw new HashErrorException(e.getMessage());
		} catch (InvalidKeySpecException e) {
			throw new HashErrorException(e.getMessage());
		}

		model.setPassword(hashPassword);
		Usuario user = userRepository.save(model);
		if(user != null) {
			user.setPassword(null);
		}		
		return user;
	}

	public Usuario update(Usuario model, String id) throws HashErrorException, DuplicatedException {

		Usuario userFound = loadUserById(id);
		if(userFound== null) {
			throw new NotFoundException("Usuário não encontrado");
		}

		if(!model.getPassword().isEmpty()) {
			String hashPassword="";

			try {
				hashPassword = this.hashPassword(model.getPassword());
			} catch (NoSuchAlgorithmException e) {
				throw new HashErrorException(e.getMessage());
			} catch (InvalidKeySpecException e) {
				throw new HashErrorException(e.getMessage());
			}

			model.setPassword(hashPassword);
		}


		Usuario user = userRepository.save(model);
		if(user != null) {
			user.setPassword(null);
		}		



		return user;
	}


	private String hashPassword(String clearPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {

		int cost = 16;

		byte[] salt = new byte[16];

		salt = "Eng@$00720191234".getBytes();

		byte[] dk = pbkdf2(clearPassword.toCharArray(), salt, 1 << cost);
		byte[] hash = new byte[salt.length + dk.length];
		System.arraycopy(salt, 0, hash, 0, salt.length);
		System.arraycopy(dk, 0, hash, salt.length, dk.length);
		Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
		return enc.encodeToString(hash);

	}
	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {

		String ALGORITHM = "PBKDF2WithHmacSHA1";
		int SIZE = 128;

		KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
			return f.generateSecret(spec).getEncoded();
		}
		catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
		}
		catch (InvalidKeySpecException ex) {
			throw new IllegalStateException("Invalid SecretKeyFactory", ex);
		}
	}



}
