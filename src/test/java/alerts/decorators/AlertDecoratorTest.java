package alerts.decorators;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.BasicAlert;
import com.alerts.decorators.PriorityAlertDecorator;
import com.alerts.decorators.RepeatedAlertDecorator;

class AlertDecoratorTest {

    @Test
    void testPriorityDecorator() {
        Alert baseAlert = new BasicAlert("1", "Critical Condition", 1000L);
        Alert decoratedAlert = new PriorityAlertDecorator(baseAlert);
        
        assertEquals("1", decoratedAlert.getPatientId());
        assertEquals(1000L, decoratedAlert.getTimestamp());
        assertEquals("Critical Condition [HIGH PRIORITY]", decoratedAlert.getCondition());
    }

    @Test
    void testRepeatedDecorator() {
        Alert baseAlert = new BasicAlert("2", "Warning", 2000L);
        Alert decoratedAlert = new RepeatedAlertDecorator(baseAlert);
        
        assertEquals("2", decoratedAlert.getPatientId());
        assertEquals(2000L, decoratedAlert.getTimestamp());
        assertEquals("Warning (Repeated)", decoratedAlert.getCondition());
    }

    @Test
    void testChainedDecorators() {
        Alert baseAlert = new BasicAlert("3", "Failure", 3000L);
        Alert repeatedAlert = new RepeatedAlertDecorator(baseAlert);
        Alert priorityRepeatedAlert = new PriorityAlertDecorator(repeatedAlert);
        
        assertEquals("3", priorityRepeatedAlert.getPatientId());
        assertEquals(3000L, priorityRepeatedAlert.getTimestamp());
        assertEquals("Failure (Repeated) [HIGH PRIORITY]", priorityRepeatedAlert.getCondition());
    }
}
