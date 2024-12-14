package org.ahroo.nexus;

import static org.ahroo.nexus.util.Colors.ANSI_FG_GREEN;
import static org.ahroo.nexus.util.Colors.ANSI_RESET;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class NexusApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusApplication.class, args);
    }

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        log.info(ANSI_FG_GREEN + "   **************************" + ANSI_RESET);
        log.info(ANSI_FG_GREEN + "   *** Nexus Server ready ***" + ANSI_RESET);
        log.info(ANSI_FG_GREEN + "   **************************" + ANSI_RESET);
    }
}
