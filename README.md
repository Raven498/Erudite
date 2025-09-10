# Erudite

## Description - Overall Scope
This project is for one Erudite agent, a new type of AI agent that tries to improve upon 
and integrate different learning algorithms together to develop an agent capable of generalizing
in multiple diverse subjects. Primarily, Erudite follows symbolic approaches to artificial intelligence,
generally by creating algorithms that can more thoroughly, expansively, and rigorously model information.
While most current types of AI systems (such as neural networks, large language models (LLMs), etc.) 
can predict patterns within volumes of data usually represented in numbers or text, 
these systems do not carry any fundamental understanding of the actual 
concepts and knowledge they are operating on. To address this, symbolic AI is another
active area of research that can potentially allow AI agents to additionally understand
the underlying concepts represented by the information they are trained on, 
and therefore can learn and use their respective properties. As a basic example, 
while LLMs will know an apple simply by the word "apple," a symbolic implementation 
will know an apple according to its unique properties and/or behaviors, 
such as its color, nutritional information, etc.

Erudite is an attempted implementation of symbolic AI, using a new architecture that integrates 
different paradigms of AI together to also provide expansive coverage over 
diverse areas and types of knowledge. The ultimate goal is to provide the agent the ability to 
efficiently and effectively generalize over several subjects, and develop a 
common "knowledge base" that allows for cross-domain learning and applications of knowledge. 
It will also use symbolic learning algorithms to apply the symbolic approach to 
the knowledge it learns, eventually forming an overall object-oriented hierarchy 
that describes different objects' properties and the relationships between them.

## Description - Current Scope
The current scope of this project is more focused around the technical infrastructure 
for running a network of Erudite agents (called the "Erudite network") and creating data 
pipelines that persist and transfer data across the network. This "data" includes ground-truth 
knowledge received from the environment interface (see the "erudite-env-interface" repo for more information on this)
and generalizations/predictions (which, in Erudite terminology, are called "approximations")
that are created by individual Erudite agents. This scope encompasses a variety of features that ultimately contribute to 
the overall technical and network architecture used for running the Erudite network, and these are currently . It doesn't yet 
include the actual learning algorithms needed to generate approximations by each agent. These are currently still in 
isolated development via local scripts and will be added to this repo later on in other feature branches.

### Technical Details
The network (composed of Erudite agents) communicates with the "environment interface" (EI), which trains the network and 
provides prompts and other contextual data for the network to use when solving specific problems or conducting 
operations within its environment. For this implementation, the EI is a separate SpringBoot server that communicates with 
both the Gemini LLM (via the Gemini API) and the Erudite network to construct training data and problems (using Gemini) 
and send it to the network for processing. The interface communicates bidirectionally - it also, for example, receives 
approximations from the network and passes them to Gemini for evaluation, which it then passes back to the agents. In 
other words, the EI is the intermediary between Gemini and the network, with Gemini serving as the "teaching" system by: 
1. Constructing training information and practice problems
2. Continuously evaluating the network's performance
3. Providing real-world prompts and contexts that require solutions from Erudite

While the network serves as the "learning" system that:
1. Uses Gemini training and evaluations to generalize to other contexts and environments
2. Interacts correctly with outside environmental elements via Gemini
3. Develops a cumulative knowledge base of approximations that accurately and completely fit patterns in the overall external environment

See the README feature/async-propagation for more information about the specific work being done under this scope.