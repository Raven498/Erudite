# Erudite: Async Propagation

## Description
This branch implements a feature called "async propagation" to allow for the efficient and 
collective transmission of approximations within a network of Erudite agents. This feature allows 
Erudite agents within the network to "chain" different approximations together in a pipeline 
format. Async propagation is started when an initial agent receives ground-truth knowledge 
from the environment interface, and then builds an approximation based off this information. 
The agent will then send this approximation to every other registered agent in the network, 
where each agent will build another approximation based off the previous approx, then send it to 
every other agent again. This process is repeated until a configurable maximum number of cycles is hit, 
upon which the final approximation is sent back to the initial agent, which persists its data to the 
database. From an overall perspective, this allows for more thorough/efficient approximations and generalizations 
to be generated off of one set of true knowledge, as learning/training/analysis operations are distributed across 
various agents. Additionally, if more creative/generative operations are used for creating approximations, this will 
allow for an expanded range of potential final outputs as agents continue chaining these operations together.

### Technical Details & Future Development
Tech Stack: SpringBoot, PostgreSQL, Hibernate + JPA
Each agent runs a SpringBoot REST controller that contains endpoints for receiving & propagating
data, requesting data from the environment interface, and persisting data to the PostgreSQL database. 
Hibernate and JPA are used for all reading & writing to the Postgres database server.

Currently, to sync IP addresses across all machines, the Postgres database contains a table for
registering IPs for all host machines. Right after SpringBoot startup, each agent will register its host machine IP 
to the table. During propagation, agents pull IP information from this registry to send data accordingly.
A separate endpoint also exists for deleting IPs if a host machine is removed.

In the future, I'm currently planning on deploying this architecture via Kubernetes, and use its API
to handle IP registry and discovery. The Postgres database server will still be used to persist agent data, but each 
agent will instead run in a Kubernetes pod off a SpringBoot image. The async propagation algorithm will be maintained in 
both Kubernetes and non-Kubernetes applications, but IP discovery would work with the Kubernetes API instead.
