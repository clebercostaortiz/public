package br.com.comprasa2.apicore.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.comprasa2.apicore.exception.DuplicatedException;
import br.com.comprasa2.apicore.model.JwtResponse;
import br.com.comprasa2.apicore.model.Usuario;
import br.com.comprasa2.apicore.service.UserService;
import br.com.comprasa2.apicore.util.JwtTokenUtil;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userDetailsService;

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	private static final String USER_DEFAULT="usrA2";
	private static final String USER_PWD="Eng@$Cadastro";
	

	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(HttpServletRequest request) throws Exception {
		Usuario basicInfo = jwtTokenUtil.getBasicInfo(request);
		if (basicInfo == null) {
			return new ResponseEntity<String>("Credenciais inválidas", HttpStatus.UNAUTHORIZED);
		}
		
		Thread.sleep(3000);

		final Usuario userDetails = userDetailsService
				.loadUserByUsernameAndPass(basicInfo.getUsername(), basicInfo.getPassword());
		
		if(userDetails == null) {
			return new ResponseEntity<String>("Credenciais inválidas", HttpStatus.UNAUTHORIZED);
		}
		
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	
	@PostMapping(value = { "/",  ""})
	public ResponseEntity<?> insert(HttpServletRequest request, @RequestBody Usuario dto) {
		try {
			
			Usuario basicInfo = jwtTokenUtil.getBasicInfo(request);
			if (basicInfo == null || !USER_DEFAULT.equals(basicInfo.getUsername()) || !USER_PWD.equals(basicInfo.getPassword())) {
				return new ResponseEntity<String>("Credenciais inválidas", HttpStatus.UNAUTHORIZED);
			}
			
			return ResponseEntity.ok(userDetailsService.insert(dto));
		} catch (DuplicatedException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value = { "/{id-user}"})
	public ResponseEntity<?> update(@RequestBody Usuario dto, @PathVariable("id-user") String idUser) {
		try {
			return ResponseEntity.ok(userDetailsService.update(dto, idUser));
		} catch (NotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@GetMapping(value = { "/check-exist", "/check-exist/" })
	public ResponseEntity<?> getById(@RequestParam(value = "email-or-phone", required = true) String emailOrPhone){
		try {
			return ResponseEntity.ok(userDetailsService.checkUserByEmailOrPhone(emailOrPhone));
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@PostMapping(value = { "/{id-user}/user"})
	public ResponseEntity<?> addUserToList(@PathVariable("id-user") String idUser,
			@RequestParam(value = "email-or-phone", required = true) String emailOrPhone			
			) {
		try {
			return ResponseEntity.ok(userDetailsService.addUserToUser(emailOrPhone, idUser));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@DeleteMapping(value = { "/{id-user}/user"})
	public ResponseEntity<?> removeUserToList(@PathVariable("id-user") String idUser,
			@RequestParam(value = "email-or-phone", required = true) String emailOrPhone			
			) {
		try {
			return ResponseEntity.ok(userDetailsService.removeUserFromUser(emailOrPhone, idUser));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping(value = { "/{id-user}/user"})
	public ResponseEntity<?> getUsersByIdLista(@PathVariable("id-user") String idUser){
		try {
			return ResponseEntity.ok(userDetailsService.loadUsersFromUser(idUser));
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	

	
	
	
	


}
