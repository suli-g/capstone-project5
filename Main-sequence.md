```mermaid
sequenceDiagram
    title Project creation
    actor usr as User
    participant app as Application
    participant project as Project
    participant acc as Account
    participant person as PersonFactory
    app ->>usr: Request Project Details

    usr ->>+app: details
    Note right of usr: projectName
    Note right of usr: projectType
    Note right of usr: projectAddress
    Note right of usr: projectErfNumber
    app ->> project: details
    app ->>-usr: Project created

    app ->> usr: Request Account Details
    usr ->>+app: details
    Note right of usr: totalCost
    Note right of usr: totalPaid
    app ->> acc: details
    acc ->> app: outstanding
    app ->>-usr: Provide Account Details

    app ->>usr: Request Personnel Details
    usr ->>app: customerDetails
    app ->> person: details
    activate person
    usr ->>app: contractorDetails
    person ->> person: create
    Note right of person: See Person-sequence.md
    person -->> app: Person
    deactivate person

    app ->> person: details
    activate person
    usr ->>app: architectDetails
    person ->> person: create
    Note right of person: See Person-sequence.md
    person -->> app: Person
    deactivate person

    app ->> person: details
    activate person
    usr ->>app: architectDetails
    person ->> person: create
    Note right of person: See Person-sequence.md
    person -->> app: Person
    deactivate person

    app -->> usr: Invoice
```