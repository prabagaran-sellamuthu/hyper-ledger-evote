package org.hyperledger.fabric.dcu.evote;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import com.owlike.genson.Genson;

@Contract(name = "VotingChainCode", info = @Info(title = "Voting App", description = "The hyperlegendary Voting Chain Code App", version = "0.0.1-SNAPSHOT", license = @License(name = "DCU College Assingment", url = "https://www.dcu.ie"), contact = @Contact(email = "prabagaran.sellamuthu2@mail.dcu.ie", name = "Prabagaran Sellamuthu", url = "https://www.dcu.ie/iss/student-my-page")))
@Default
public final class VotingChainCode implements ContractInterface {

    private final Genson genson = new Genson();

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void registerVoter(final Context ctx, final String voterId, final String firstName, final String lastName,
            final String registrarId) {
        ChaincodeStub stub = ctx.getStub();
        String hasVoted = "";

        Voter voter = new Voter(voterId, firstName, lastName, registrarId, hasVoted);
        String voterInfo = genson.serialize(voter);

        String existingVoterInfo = stub.getStringState(voterId);
        if (existingVoterInfo != null && !existingVoterInfo.isEmpty()) {
            throw new RuntimeException("This voter is already registered");
        }

        stub.putStringState(voterId, voterInfo);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void registerParty(final Context ctx, final String partyName) {
        ChaincodeStub stub = ctx.getStub();

        Party party = new Party(partyName, 0); // Initialize the vote count to 0
        String partyInfo = genson.serialize(party);

        String existingPartyInfo = stub.getStringState(partyName);
        if (existingPartyInfo != null && !existingPartyInfo.isEmpty()) {
            throw new RuntimeException("This party is already registered");
        }

        stub.putStringState(partyName, partyInfo);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void castVote(final Context ctx, final String voterId, final String partyName) {
        ChaincodeStub stub = ctx.getStub();

        String partyInfo = stub.getStringState(partyName);
        if (partyInfo == null || partyInfo.isEmpty()) {
            String errorMessage = String.format("Party %s does not exist", partyName);
            System.out.println(errorMessage);
        }
        Party party = genson.deserialize(partyInfo, Party.class);


        // Increment the vote count for the party
        int newPartyVotes = party.getVoteCount() + 1;
        Party updatdParty = new Party(partyName, newPartyVotes);
        String updatedPartyInfo = genson.serialize(updatdParty);
        stub.putStringState(party.getPartyName(), updatedPartyInfo);

        // Check if the voter has already voted
        String voterInfo = stub.getStringState(voterId);
        if (voterInfo == null || voterInfo.isEmpty()) {
            System.out.println("Voter not found");
            throw new ChaincodeException("Voter not found");
        }
        Voter voter = genson.deserialize(voterInfo, Voter.class);
        if (!voter.getHasVoted().isEmpty()) {
            System.out.println("User already voted");
            throw new ChaincodeException("User already voted");
        }

        // Record the vote for the voter
        Voter updateVoter = new Voter(voter.getVoterId(), voter.getFirstName(), voter.getLastName(), voter.getRegistrarId(), partyName);
        String updatedVoterInfo = genson.serialize(updateVoter);
        stub.putStringState(voter.getVoterId(), updatedVoterInfo);
    }

    // Retrieve voter information from the ledger
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    private Voter getVoter(final Context ctx, final String voterId) {
        ChaincodeStub stub = ctx.getStub();
        String voterInfo = stub.getStringState(voterId);
        if (voterInfo == null || voterInfo.isEmpty()) {
            throw new RuntimeException("Voter not found");
        }
        return genson.deserialize(voterInfo, Voter.class);
    }

    // Retrieve party information from the ledger
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    private Party getParty(final Context ctx, final String partyName) {
        ChaincodeStub stub = ctx.getStub();
        String partyInfo = stub.getStringState(partyName);
        if (partyInfo == null || partyInfo.isEmpty()) {
            return null; // Party not found
        }
        return genson.deserialize(partyInfo, Party.class);
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String checkPoll(final Context ctx, final String partyName) {
        ChaincodeStub stub = ctx.getStub();

        String partyVotes = stub.getStringState(partyName);
        if (partyVotes == null || partyVotes.isEmpty()) {
            throw new RuntimeException("This party is not registered");
        }

        return partyName + ": " + partyVotes;

    }
}
