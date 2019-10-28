package br.com.comprasa2.apicore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.comprasa2.apicore.model.Log;
import br.com.comprasa2.apicore.service.LogService;

@RestController
@CrossOrigin
@RequestMapping("/log")
public class LogController {
	
	@Autowired
	private LogService logService;

	private static Logger logger = LoggerFactory.getLogger(LogController.class);
	
	
	@PostMapping(value = { "/",  ""})
	public ResponseEntity<?> insert(@RequestBody Log dto) {
		try {
			return ResponseEntity.ok(logService.insert(dto));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}
