# Say Something Smart

Say Something Smart (SSS) is a dialogue engine based on a large corpus of movie subtitles (Subtle). SSS receives a user request and retrieves an answer from a set of candidates that are retrieved from the corpus using Lucene. The selection of the best answer among those candidates is made according to a set of weighted criteria, whose weights are previously defined.

The current version also includes a couple of modes to experiment adjusting the feature weights iteratively: those are the _learning_ mode (where weight adjustment takes place using a training corpus) and the _evaluation_ mode (where the accuracy of the learned weights is evaluated using a test corpus).

## First steps

# Code
Consider the directory where the code is (without the libs and the corpus)
Create the lib directory there and add all the lib files to that directory
Do:
ant create-jar

# Corpus
In what concerns the corpus:
Add it to resources/corpus/<YOURDIR>
Named it with the language of the corpus (currently, only "eng.txt" or "por.txt" are possible)
In the config file resources/config/config.xml add the path to that file considering the tag <corpus> and also <dbPath> and <indexPath>
The first time you run SSS with a new corpus, put false in the tag <usePreviouslyCreatedIndex>; after that (Lucene indexes the corpus) put the tag to true

## How to run

_[] denotes optional parameters_

**Dialogue (default mode):**

`java -jar sss.jar [mode=dialogue] [questions_file] [> output file]`

**Learning:**

`java -jar sss.jar mode=learning parameters_file [> output_file]`

**Evaluation:**

`java -jar sss.jar mode=evaluation weights_file [> output_file]`


## Configurations

In the file `config.xml`, several settings should be configured before running SSS:

### Language

- `<language>`: `english` or `portuguese`.

### Lucene

- `<dbPath>`: Path (relative to `SSS` folder) to the corpus database file (`db.db4o`).
- `<indexPath>`: Path (relative to `SSS` folder) where the corpus indices are stored. Should end with a folder with the same name as the language selected. (portuguese? ou por? <------- nao percebo)
- `<usePreviouslyCreatedIndex>`: `true` if the previously created indices and database. should be reused, `false` otherwise.
- `<hitsPerQuery>`: Number of candidates retrieved by Lucene.

### Resources
- `<corpus>`: Path (relative to `SSS` folder) to the corpus that should be indexed.
- `<stopwords>`: Path (relative to `SSS` folder) for the list of stopwords for the selected language.
- `<importantPersons>`: Path (relative to `SSS` folder) for a list of names of public figures.
- `<ner>`:  Path (relative to `SSS` folder) to a Named Entity Recognition classifier.
- `<tagger>`: Path (relative to `SSS` folder) to a Part-of-Speech tagger.
- `<wordnetDict>`: Path (relative to `SSS` folder) to WordNet.
- `<log>`: Path (relative to `SSS` folder) for the log of the conversation in the selected language.

### Normalizers

 - `<normalizers>`: Names of the normalizers to be used (depending on the selected language), separated by commas. They should match the names in the `NormalizerFactory`).

### Similarity

- `<similarityMeasure>`: The similarity measure to be used in some of the criteria. Should match one of the names in the `SimilarityMeasureFactory`.

### Criteria

- `<criterion>`: Each criterion that will be used to select the best answer, identified by its `name` (should match the name in the `QaScorerFactory`). Each criterion has a `weight` (an integer value between 0 and 100). The sum of the criterion weights should be equal to 100. The `SimpleConversationContext` criterion is also characterized by the `nPreviousInteractions` to consider.

### "No answer found" messages

- `<noAnswerFound>` / `<noAnswerFoundMsgsEN>`: List of possible messages when no answer was retrieved (being `<msg>` each message).


### Learning and evaluation configurations

The following configurations are only relevant for the `learning` and `evaluation` modes, and concern the use of the Cornell Movie-Dialog corpus.

- `<folder>`: Folder (relative to `SSS` folder) where the files from the corpus are stored.
- `<interactions>`: Name of the file with the corpus of movie interactions (groups of lines).
- `<lines>`: Name of the file with the corpus of movie lines.
- `<inputSize>`: Maximum number of interactions to consider.


## Citations

### Online learning

- Mendonça, V., Melo, F. S., Coheur, L., & Sardinha, A. (2017). Online learning for conversational agents. In Lecture Notes in Computer Science (including subseries Lecture Notes in Artificial Intelligence and Lecture Notes in Bioinformatics) (Vol. 10423 LNAI, pp. 739–750). http://doi.org/10.1007/978-3-319-65340-2_60

- Mendonça, V., Melo, F. S., Coheur, L., & Sardinha, A. (2017). A Conversational Agent Powered by Online Learning. In Proceedings of the 16th Conference on Autonomous Agents and MultiAgent Systems (pp. 1637–1639). São Paulo, Brazil: International Foundation for Autonomous Agents and Multiagent Systems. Retrieved from http://dl.acm.org/citation.cfm?id=3091282.3091388


### Dialogue

- Magarreiro, D., Coheur, L., & Melo, F. S. (2014). Using subtitles to deal with Out-of-Domain interactions. In SemDial 2014 - DialWatt.

- Ameixa, D., Coheur, L., Fialho, P., & Quaresma, P. (2014). Luke, I am your father: Dealing with out-of-domain requests by using movies subtitles. In Proceedings of the 14th International Conference on Intelligent Virtual Agents (IVA’14) (Vol. 8637 LNAI, pp. 13–21). http://doi.org/10.1007/978-3-319-09767-1_2

