# HeFQUIN-PGConnector
Module of the [HeFQUIN query federation engine](https://github.com/LiUSemWeb/HeFQUIN) to connect to Property Graph data sources. In particular, this module provides the functionality to run SPARQL-star queries over user-configurable RDF-star views of Property Graphs by translating these queries into openCypher queries which, then, are sent directly to a Property Graph store (we only support Neo4j at the moment).

This module also comes with two command-line programs:
* **RunBGPOverNeo4j** provides the SPARQL-star query functionality for Property Graphs as a standalone tool, and
* **MaterializeRDFViewOfLPG** can be used to convert the Property Graphs into an RDF-star graphs by materializing the RDF-star views that are otherwise considered only as virtual views by the query functionality.

While documenting the query-translation functionality implemented by this module is still work in progress, the (user-configurable) Property Graph to RDF-star mapping that defines the RDF-star views is described below, followed by a description of the two command-line programs.

## User-Configurable Mapping of Property Graphs to RDF-star Graphs
TODO

## Command-Line Programs
### Compile the Programs
To use the command-line programs you first need to compile them using the Java source code in this repository.

To this end, first create a local clone of this repository on your computer, which you can do by opening a terminal, navigate to the directory into which you want to clone the repository, and then either [clone the repository with its SSH URL](https://docs.github.com/en/get-started/getting-started-with-git/about-remote-repositories#cloning-with-ssh-urls) by running the following command
```
git clone git@github.com:LiUSemWeb/HeFQUIN-PGConnector.git
```
or [clone the repository with its HTTPS URL](https://docs.github.com/en/get-started/getting-started-with-git/about-remote-repositories#cloning-with-https-urls) by running the following command
```
git clone https://github.com/LiUSemWeb/HeFQUIN-PGConnector.git
```
Both of these two commands should give you a sub-directory called `HeFQUIN-PGConnector`. Enter this directory and use Maven to compile the Java source code by running the following command
```
mvn package
```
Assuming the Maven process completes successfully, you can use the command-line programs provided in this repository as described in the following sections.

### Run the RunBGPOverNeo4j Program
RunBGPOverNeo4j provides the query functionality of HeFQUIN-PGConnector as a standalone tool. As a Java program, it needs to be run using the Java interpreter (after compiling it [as described above](#compile-the-programs)). The default way to do so is by running the following command,
```
java -cp target/HeFQUIN-PGConnector-0.0.1-SNAPSHOT.jar se.liu.ida.hefquin.cli.RunBGPOverNeo4j --query=query.rq --lpg2rdfconf=LPG2RDF.ttl --endpoint=http://localhost:7474/db/neo4j/tx/commit
```
where the `--query` argument is used to refer to a file that contains the SPARQL-star query you want execute, the `--lpg2rdfconf` argument refers to an RDF document that provides an RDF-based description of the LPG-to-RDF-star configuration that you want to use for the RDF-star view of your Property Graph (an example of an RDF Turtle file with such a description is provided as part of this repository and, thus, available in your local clone of the repository---see: `ExampleLPG2RDF.ttl`), and the `--endpoint` argument specifies the HTTP address at which your Neo4j instance responds to Cypher queries over your Property Graph.

Further arguments can be used. To see a list of all arguments supported by the program, run the program with the `--help` argument:
```
java -cp target/HeFQUIN-PGConnector-0.0.1-SNAPSHOT.jar se.liu.ida.hefquin.cli.RunBGPOverNeo4j --help
```

### Run the MaterializeRDFViewOfLPG Program
MaterializeRDFViewOfLPG can be used to convert the Property Graphs into an RDF-star graphs by applying the user-configurable Property Graph to RDF-star mapping. As a Java program, it needs to be run using the Java interpreter (after compiling it [as described above](#compile-the-programs)). The default way to do so is by running the following command,
```
java -cp target/HeFQUIN-PGConnector-0.0.1-SNAPSHOT.jar se.liu.ida.hefquin.cli.MaterializeRDFViewOfLPG --lpg2rdfconf=LPG2RDF.ttl --endpoint=http://localhost:7474/db/neo4j/tx/commit
```
where the `--lpg2rdfconf` argument refers to an RDF document that provides an RDF-based description of the LPG-to-RDF-star configuration that you want to use for the RDF-star view of your Property Graph (an example of an RDF Turtle file with such a description is provided as part of this repository and, thus, available in your local clone of the repository---see: `ExampleLPG2RDF.ttl`) and the `--endpoint` argument specifies the HTTP address at which your Neo4j instance responds to Cypher queries over your Property Graph.

Further arguments can be used. To see a list of all arguments supported by the program, run the program with the `--help` argument:
```
java -cp target/HeFQUIN-PGConnector-0.0.1-SNAPSHOT.jar se.liu.ida.hefquin.cli.MaterializeRDFViewOfLPG --help
```
