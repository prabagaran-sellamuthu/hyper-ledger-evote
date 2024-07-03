# Hyperledger eVote

## Prerequisites

Before getting started, ensure that your system meets the following prerequisites:

- Git — version 2.20 or greater (`$ git --version`)
- cURL — version 7.80.0 or greater (`$ curl --version`)
- Docker — version 17.06.2-ce or greater (`$ docker --version`)
- Docker Compose — version 1.27.4 or greater (`$ docker-compose --version`)
- Go programming language — version 1.20.0 (`$ go version`)
- Java programming language — version jdk-11 (`$ java -version`)

For installation instructions, refer to: [Setup Hyperledger Fabric 2.5 LTS on Ubuntu 22.04 Linux](<https://medium.com/@immortalsaint/setup-hyperledger-fabric-2-5-lts-on-ubuntu-22-04-linux-f60163281f0c>)

## Installation

Pull the Fabric installation script file and change the access mode:

```bash
curl -sSLO https://raw.githubusercontent.com/hyperledger/fabric/main/scripts/install-fabric.sh && chmod +x install-fabric.sh
```

Run the installation script:

```bash
./install-fabric.sh docker samples binary
```

## Getting Started

1.Unzip your project code base and navigate to the test network:

```bash
cd hyper-ledger-evote
cd test-network
```
2.Spin up a sample network with a channel named "mychannel" and certificate authority (CA):

```bash
./network.sh up createChannel -ca -c mychannel
```
3.Deploy the VotingChainCode

```bash
./network.sh deployCC -ccn VotingChainCode -ccp ../application/chaincode-java/ -ccl java
```

4.Run the eVote Java application using the FABRIC Java SDK to interact with the deployed chain code:

```bash
cd ../application/application-java
./gradlew runApp
```

## Usage

- Upon running the application, both Admin and javaApp users will be registered under ORG-1.
- Transactions will flow to the Fabric network.
- To monitor transactions, install Hyperledger Fabric network explorer. For instructions, refer to Hyperledger Fabric Network Explorer README.
https://github.com/hyperledger-labs/blockchain-explorer/blob/main/README.md

## Let's break down the functionality implemented in the App.java file provided:

- Connect to the Gateway: The connect() method establishes a connection to the Hyperledger Fabric network using the credentials stored in the wallet and the network configuration file.

- Enroll Admin and Register User: This step ensures that the necessary administrative and user identities are enrolled and registered with the Fabric network. It invokes the EnrollAdmin and RegisterUser classes to perform these actions.

- Perform Voter Registration: The application simulates the registration of voters by invoking the registerVoter() method. It registers 10 voters, each with a unique voter ID, first name, last name, and registrar ID.

- Register Political Parties: Next, the application registers three political parties by invoking the registerParty() method. It registers parties named "PartyA", "PartyB", and "PartyC".

- Initiate Voting: After registering voters and parties, the application starts the voting process. It randomly casts votes for the registered parties on behalf of each voter using the castVote() method.

- Check Voting Results: Finally, the application evaluates the voting results for each political party by invoking the checkPoll() method. It retrieves and displays the poll results for each party.

- Each step is accompanied by informative messages printed to the console, providing feedback on the progress and outcome of the actions performed by the application.

- When you run the eVote Java application (App.java), it automates the process of voter and party registration, voting, and result evaluation within the Hyperledger Fabric network.

## Architecture Diagram 
![image](https://github.com/prabagaran-sellamuthu/hyper-ledger-evote/assets/38026104/ae9983ef-aea5-45a7-a941-0ba699891870)

## Further Assistance
For further assistance or inquiries, please contact Prabagaran Sellamuthu
