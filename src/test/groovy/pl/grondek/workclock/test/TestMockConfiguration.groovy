package pl.grondek.workclock.test

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pl.grondek.workclock.repository.EventRepository
import pl.grondek.workclock.repository.WorkDayRepository
import spock.mock.DetachedMockFactory

@TestConfiguration
class TestMockConfiguration {

    private DetachedMockFactory factory = new DetachedMockFactory()

    @Bean
    @ConditionalOnMissingBean
    WorkDayRepository mockWorkDayRepository(){
        return factory.Mock(WorkDayRepository)
    }

    @Bean
    @ConditionalOnMissingBean
    EventRepository mockEventRepository(){
        return factory.Mock(EventRepository)
    }
}
