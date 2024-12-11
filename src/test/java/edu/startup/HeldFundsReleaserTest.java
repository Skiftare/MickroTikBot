package edu.startup;

import edu.Data.DataManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class HeldFundsReleaserTest {
    
    @Test
    public void testSuccessfulRelease() {
        DataManager mockDataManager = mock(DataManager.class);
        HeldFundsReleaser releaser = new HeldFundsReleaser(mockDataManager);
        
        releaser.releaseAllHeldFunds();
        
        verify(mockDataManager, times(1)).releaseAllHeldFunds();
    }
    
    @Test
    public void testFailedRelease() {
        DataManager mockDataManager = mock(DataManager.class);
        doThrow(new RuntimeException("Test error"))
            .when(mockDataManager)
            .releaseAllHeldFunds();
            
        HeldFundsReleaser releaser = new HeldFundsReleaser(mockDataManager);
        
        try {
            releaser.releaseAllHeldFunds();
        } catch (RuntimeException e) {
            verify(mockDataManager, times(1)).releaseAllHeldFunds();
            assertEquals("Failed to release held funds during startup", e.getMessage());
            assertNotNull(e.getCause());
            assertEquals("Test error", e.getCause().getMessage());
        }
    }
}
