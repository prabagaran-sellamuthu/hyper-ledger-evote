package application.java;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;


public class App {

	private static final String CHANNEL_NAME = System.getenv().getOrDefault("CHANNEL_NAME", "mychannel");
	private static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "VotingChainCode");

	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

	// helper function for getting connected to the gateway
	public static Gateway connect() throws Exception{
		// Load a file system based wallet for managing identities.
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		// load a CCP
		Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "javaAppUser").networkConfig(networkConfigPath).discovery(true);
		return builder.connect();
	}

	public static void main(String[] args) throws Exception {
		// enrolls the admin and registers the user
		try {
			EnrollAdmin.main(null);
			RegisterUser.main(null);
		} catch (Exception e) {
			System.err.println(e);
		}

		try (Gateway gateway = connect()) {

			Network network = gateway.getNetwork(CHANNEL_NAME);
			Contract contract = network.getContract(CHAINCODE_NAME);

			System.out.println("******************************************");
			System.out.println("***** Voters Registration on ORG1 ********");
			System.out.println("******************************************");	

		for (int i = 1; i <= 10; i++) {
            String voterId = "DCU-Voter" + i;
            String firstName = "John";
            String lastName = "Doe";
            String registrarId = "registrar456";
            registerVoter(contract, voterId, firstName, lastName, registrarId);
        }

		
		System.out.println("******************************************");
		System.out.println("*********** Party Registration ***********");
		System.out.println("******************************************");

        // // Register 3 parties
        String[] parties = {"PartyA", "PartyB", "PartyC"};
        for (String party : parties) {
            registerParty(contract, party);
        }

		System.out.println("******************************************");
		System.out.println("*********** Voting Starts ****************");
		System.out.println("******************************************");

        // // Cast random votes
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            String voterId = "DCU-Voter" + i;
            String party = parties[random.nextInt(3)];
            castVote(contract, voterId, party);
        }

		System.out.println("******************************************");
		System.out.println("*********** Voting Results **************");
		System.out.println("******************************************");
        // Check poll results for each party
        for (String party : parties) {
            checkPoll(contract, party);
        }
	} catch (Exception e) {
		System.err.println(e);
		System.exit(1);
	}

    }

    private static void registerVoter(Contract contract, String voterId, String firstName, String lastName, String registrarId) throws ContractException, TimeoutException, InterruptedException {
        System.out.println("Registering a voter..."+voterId);
        contract.submitTransaction("registerVoter", voterId, firstName, lastName, registrarId);
        System.out.println("Voter registered successfully.");
    }

    private static void registerParty(Contract contract, String partyName) throws ContractException, TimeoutException, InterruptedException {
        System.out.println("Registering a party..."+partyName);
        contract.submitTransaction("registerParty", partyName);
        System.out.println("Party registered successfully.");
    }

    private static void castVote(Contract contract, String voterId, String party) throws ContractException, TimeoutException, InterruptedException  {
        System.out.println("Casting a vote by..." +voterId );
        contract.submitTransaction("castVote", voterId, party);
        System.out.println("Vote cast successfully.");
    }

    private static void checkPoll(Contract contract, String party) throws ContractException, TimeoutException, InterruptedException  {
        System.out.println("Checking poll result for " + party + "...");
        byte[] result = contract.evaluateTransaction("checkPoll", party);
        System.out.println("Poll result for " + party + ": " + new String(result));
    }

}
