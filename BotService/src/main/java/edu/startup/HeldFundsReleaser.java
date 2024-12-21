package edu.startup;

import java.util.logging.Logger;

import edu.Data.DataManager;

public class HeldFundsReleaser {
    private final DataManager dataManager;

    public HeldFundsReleaser(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void releaseAllHeldFunds() {
        try {
            Logger.getAnonymousLogger().info("Starting release of all held funds...");
            dataManager.releaseAllHeldFunds();
            Logger.getAnonymousLogger().info("Successfully released all held funds");
        } catch (Exception e) {
            Logger.getAnonymousLogger().severe("Error while releasing held funds: " + e.getMessage());
            throw new RuntimeException("Failed to release held funds during startup", e);
        }
    }
}
