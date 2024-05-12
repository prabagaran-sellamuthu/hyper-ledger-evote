package org.hyperledger.fabric.dcu.evote;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Voter {

    @Property()
    private final String voterId;

    @Property()
    private final String firstName;

    @Property()
    private final String lastName;

    @Property()
    private final String registrarId;

    @Property()
    private final String hasVoted; // Modify to include hasVoted attribute

    public String getVoterId() {
        return voterId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRegistrarId() {
        return registrarId;
    }

    // Add getter and setter for hasVoted attribute
    public String getHasVoted() {
        return hasVoted;
    }

    public Voter(@JsonProperty("voterId") final String voterId, @JsonProperty("firstName") final String firstName,
            @JsonProperty("lastName") final String lastName, @JsonProperty("registrarId") final String registrarId, @JsonProperty("hasVoted") final String hasVoted) {
        this.voterId = voterId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrarId = registrarId;
        this.hasVoted = hasVoted; // Initialize hasVoted attribute
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Voter other = (Voter) obj;

        return Objects.deepEquals(
                new String[] {getVoterId(), getFirstName(), getLastName()},
                new String[] {other.getVoterId(), other.getFirstName(), other.getLastName()})
                &&
                Objects.deepEquals(
                        new String[] {getRegistrarId()},
                        new String[] {other.getRegistrarId()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVoterId(), getFirstName(), getLastName(), getRegistrarId());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [voterId=" + voterId
                + ", firstName="
                + firstName + ", lastName=" + lastName + ", registrarId=" + registrarId + "]";
    }
}
