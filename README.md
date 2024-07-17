# HeFQUIN-PGConnector
Module of the [HeFQUIN query federation engine](https://github.com/LiUSemWeb/HeFQUIN) to connect to Property Graph data sources. In particular, this module provides the functionality to run SPARQL-star queries over user-configurable RDF-star views of Property Graphs by translating these queries into openCypher queries which, then, are sent directly to a Property Graph store (we only support Neo4j at the moment).

This module also comes with two command-line programs:
* [**RunBGPOverNeo4j**](#run-the-runbgpoverneo4j-program) provides the SPARQL-star query functionality for Property Graphs as a standalone tool, and
* [**MaterializeRDFViewOfLPG**](#run-the-materializerdfviewoflpg-program) can be used to convert the Property Graphs into an RDF-star graphs by materializing the RDF-star views that are otherwise considered only as virtual views by the query functionality.

While documenting the query-translation functionality implemented by this module is still work in progress, the (user-configurable) Property Graph to RDF-star mapping that defines the RDF-star views is described in the [next section](#user-configurable-mapping-of-property-graphs-to-rdf-star-graphs), followed by a [description of the two command-line programs](#command-line-programs).

## User-Configurable Mapping of Property Graphs to RDF-star Graphs
The SPARQL-star based query functionality provided by this module considers specific types of RDF-star views of an underlying Property Graph. These views are defined based on a user-configurable mapping from the Property Graph model to RDF-star.

The idea of this mapping is simple: Given a Property Graph, every edge of this graph (including the label of the edge) is represented as an ordinary RDF triple in the resulting RDF-star data. The same holds for each node with its label, as well as for every node property. Edge properties are represented as nested triples that contain, as their subject, the triple representing the corresponding edge (more details and examples below).

While the structure of the resulting RDF-star graphs cannot be influenced, the mapping is configurable in terms of the particular elements (blank nodes and IRIs) of the triples that it generates. Formally, this form of configuration is captured by a notion that we call *LPG-to-RDF-star configuration*. The concrete LPG-to-RDF-star configuration to be used when applying the mapping may, in principle, be specified in various forms. The form that the HeFQUIN-PGConnector expects is an RDF-based description using [a specific RDF vocabulary](https://github.com/LiUSemWeb/HeFQUIN-PGConnector/blob/main/vocabs/LPGtoRDFConfiguration.ttl) that we have developed for this purpose and that provides a lot of different options for the different components of LPG-to-RDF-star configurations.

The following sections describe the mapping approach in more detail and, at the same time, introduce the components of LPG-to-RDF-star configurations, together with some of the possible options for each of these components. For a complete definition of all the options, refer to [the file that defines the vocabulary](https://github.com/LiUSemWeb/HeFQUIN-PGConnector/blob/main/vocabs/LPGtoRDFConfiguration.ttl), and a formal definition of the whole mapping approach can be found in the following research paper:

* Olaf Hartig: [Foundations to Query Labeled Property Graphs using SPARQL*](http://olafhartig.de/files/Hartig_AMAR2019_Preprint.pdf). In _Proceedings of the 1st International Workshop on Approaches for Making Data Interoperable (AMAR)_, 2019.

### Node Mapping

The *first component* of every LPG-to-RDF-star configuration is a so-called *node mapping* which specifies whether the nodes of the Property Graph are mapped to blank nodes or to IRIs and, in the case of IRIs, what these IRIs look like.

If you want to use blank nodes to capture the nodes of your Property Graph in the resulting RDF-star view, then you have to use a node mapping of type `lr:BNodeBasedNodeMapping` in the RDF-based description of your LPG-to-RDF-star configuration. Hence, the relevant part of this description may look as follows (presented in RDF Turtle format, prefix declarations omitted).
```turtle
_:c  rdf:type  lr:LPGtoRDFConfiguration ;
     lr:nodeMapping [ rdf:type lr:BNodeBasedNodeMapping ] .
```

As an alternative to blank nodes, the nodes of the Property Graph may be mapped to IRIs. The specific type of node mapping that HeFQUIN-PGConnector supports for this case is `lr:IRIPrefixBasedNodeMapping` which, for each node of the Property Graph, creates an IRI by attaching the ID of the node to a common IRI prefix. The IRI prefix to be used can be specified via the `lr:prefixOfIRIs` property.

**Example:** Assume a Property Graph with two nodes which have the IDs 153 and 295, respectively. When using an LPG-to-RDF-star configuration with a node mapping as specified in the following description, then these two Property Graph nodes are mapped to the IRIs `http://example.org/node/153` and `http://example.org/node/295`, respectively.
```turtle
_:c  rdf:type  lr:LPGtoRDFConfiguration ;
     lr:nodeMapping [ rdf:type lr:IRIPrefixBasedNodeMapping ;
                      lr:prefixOfIRIs "http://example.org/node/"^^xsd:anyURI ] .
```

### Label Predicate and Node Label Mapping

TODO

### Edge Label Mapping

TODO

### Property Name Mapping

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
`RunBGPOverNeo4j` provides the query functionality of HeFQUIN-PGConnector as a standalone tool. Since `RunBGPOverNeo4j` is a Java program, it needs to be run using the Java interpreter (after compiling it [as described above](#compile-the-programs)). The default way to do so is by running the following command,
```
java -cp target/HeFQUIN-PGConnector-0.0.1-SNAPSHOT.jar se.liu.ida.hefquin.cli.RunBGPOverNeo4j --query=query.rq --lpg2rdfconf=LPG2RDF.ttl --endpoint=http://localhost:7474/db/neo4j/tx/commit
```
where the `--query` argument is used to refer to a file that contains the SPARQL-star query you want execute, the `--lpg2rdfconf` argument refers to an RDF document that provides an RDF-based description of the LPG-to-RDF-star configuration that you want to use for the RDF-star view of your Property Graph (an example of an RDF Turtle file with such a description is provided as part of this repository and, thus, available in your local clone of the repository---see: `ExampleLPG2RDF.ttl`), and the `--endpoint` argument specifies the HTTP address at which your Neo4j instance responds to Cypher queries over your Property Graph.

Further arguments can be used. To see a list of all arguments supported by the program, run the program with the `--help` argument:
```
java -cp target/HeFQUIN-PGConnector-0.0.1-SNAPSHOT.jar se.liu.ida.hefquin.cli.RunBGPOverNeo4j --help
```

### Run the MaterializeRDFViewOfLPG Program
`MaterializeRDFViewOfLPG` can be used to convert any Property Graph into an RDF-star graph by applying our [user-configurable Property Graph to RDF-star mapping](#user-configurable-mapping-of-property-graphs-to-rdf-star-graphs). As a Java program, it needs to be run using the Java interpreter (after compiling it [as described above](#compile-the-programs)). The default way to do so is by running the following command,
```
java -cp target/HeFQUIN-PGConnector-0.0.1-SNAPSHOT.jar se.liu.ida.hefquin.cli.MaterializeRDFViewOfLPG --lpg2rdfconf=LPG2RDF.ttl --endpoint=http://localhost:7474/db/neo4j/tx/commit
```
where the `--lpg2rdfconf` argument refers to an RDF document that provides an RDF-based description of the LPG-to-RDF-star configuration that you want to use for the RDF-star view of your Property Graph (an example of an RDF Turtle file with such a description is provided as part of this repository and, thus, available in your local clone of the repository---see: `ExampleLPG2RDF.ttl`) and the `--endpoint` argument specifies the HTTP address at which your Neo4j instance responds to Cypher queries over your Property Graph.

Further arguments can be used. To see a list of all arguments supported by the program, run the program with the `--help` argument:
```
java -cp target/HeFQUIN-PGConnector-0.0.1-SNAPSHOT.jar se.liu.ida.hefquin.cli.MaterializeRDFViewOfLPG --help
```
