package br.com.comprasa2.apicore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.comprasa2.apicore.model.Log;
import br.com.comprasa2.apicore.repository.LogRepository;

@Service
@Component
public class LogService {

	@Autowired
	private LogRepository logRepository;


	public Log insert(Log model)  {
		Log user = logRepository.save(model);
		return user;
	}



}
