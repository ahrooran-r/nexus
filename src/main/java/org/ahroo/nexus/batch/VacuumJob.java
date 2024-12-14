package org.ahroo.nexus.batch;

import lombok.RequiredArgsConstructor;
import org.ahroo.nexus.service.CategoryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VacuumJob {

    private final CategoryService categoryService;

    @Scheduled(cron = "${categories.vacuum}")
    public void vacuum() {
        categoryService.vacuum();
    }

}
