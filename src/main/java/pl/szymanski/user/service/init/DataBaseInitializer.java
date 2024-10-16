package pl.szymanski.user.service.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.szymanski.user.service.model.InitState;
import pl.szymanski.user.service.service.InitStateService;

@Component
public class DataBaseInitializer {

	@Autowired
	private InitStateService initStateService;
	private final Logger LOG = LoggerFactory.getLogger(DataBaseInitializer.class);
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		InitState latestInitState = initStateService.getLatestInitState();
		if(latestInitState == null || !latestInitState.isInitialized()) {
			LOG.info("Starting initialization of DB");
			InitState initState = new InitState();
			initState.setInitialized(true);
			initState.setLastUpdate(System.currentTimeMillis());
			initStateService.save(initState);
			LOG.info("DB initialized");
		} else {
			LOG.info("DB already initialized");
		}
	}
}
