package org.hyperledger.fabric.dcu.evote;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Party {

    @Property()
    private final String partyName;

    @Property()
    private final int voteCount;

    public String getPartyName() {
        return partyName;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Party(@JsonProperty("partyName") final String partyName, @JsonProperty("voteCount") final int voteCount) {
        this.partyName = partyName;
        this.voteCount = voteCount;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Party other = (Party) obj;

        return Objects.deepEquals(
                new String[] {getPartyName()},
                new String[] {other.getPartyName()})
                &&
                Objects.deepEquals(
                        new int[] {getVoteCount()},
                        new int[] {other.getVoteCount()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPartyName(), getVoteCount());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [partyName=" + partyName
                + ", voteCount=" + voteCount + "]";
    }
}
