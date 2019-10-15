package br.com.comprasa2.apicore.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.comprasa2.apicore.model.Usuario;

@Repository
public interface UserRepository extends MongoRepository<Usuario, String>{

	Usuario findOneByUserNameAndPassword(String user, String pass);
	Usuario findOneByPhoneAndPassword(String phone, String pass);
	Usuario findOneByUserName(String user);
	Usuario findOneByUserNameAndIdGoogle(String user, String idGoogle);
	Usuario findOneByPhone(String phone);
	
}
