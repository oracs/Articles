Actor模型，这个概念大家应该都了解。Actor模型的核心思想是，对象直接不会直接调用来通信，而是通过发消息来通信。每个Actor都有一个Mailbox，它收到的所有的消息都会先放入Mailbox中，然后Actor内部单线程处理Mailbox中的消息。从而保证对同一个Actor的任何消息的处理，都是线性的，无并发冲突。从全局上来看，就是整个系统中，有很多的Actor，每个Actor都在处理自己Mailbox中的消息，Actor之间通过发消息来通信

Akka框架就是实现Actor模型的并行开发框架，并且Akka框架融入了聚合、In-Memory、Event Sourcing这些概念。

Actor非常适合作为DDD聚合根。Actor的状态修改是由事件驱动的，事件被持久化起来，然后通过Event Sourcing的技术，还原特定Actor的最新状态到内存。