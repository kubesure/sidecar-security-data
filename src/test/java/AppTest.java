import org.junit.Test;

import io.kubesure.sidecar.security.data.IgniteHelper;

public class AppTest {

    @Test
    public void testCustomerData() throws Exception{
        IgniteHelper.getCustomerByID("1212");        
    }
}
