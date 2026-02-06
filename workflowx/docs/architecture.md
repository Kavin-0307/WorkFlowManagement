This is a file meant to explain what the purpose of workflowx is , who uses it and what data is needed and what layers exist.

WHAT IS WORKFLOWX?
it is meant to be a team management software . Teams usually have various discrepancies in the workflow how they want to execute stuff as well as various requests that employees make that usually get lost in the bundle of papers on the desks of managers. I try to mitigate the situation by:
Allowing team leaders to create a detailed plan step by step, outlining the goals(I think scrum masters iirc can use it).
It also creates a sort of dependency layer. The best way i can explain is that if a completes something only then can b start right, so this allows leaders to plan acordingly(i think i will use priority levels for this)
The members can then access what belongs to them like the work they need to do .
Also make request regarding leaves, reimbursing expenses(in cases of travel or something) and everything. 

USAGE:
this is best used for smaller teams who are looking to cut through the red tape and basically increase efficiency. I hope it can allow teams to organize themselves accordingly.

DATA NEEDED(ENTITY DESIGN)(There is no separate Employer entity. “Employer” refers to a Member with Manager-level permissions.)
- Member
Name
EmployeeId(the one given by the company)
Classification(like swe)
id(used in the database)
/*Work related fields will be stored in the work entity
Work done basically
work to be done/assigned
time logged on a certain thing
*/
Active status flag

-Employer
Name
EmployerId
Client list(basically who they have to deliver to ,maybe even their own company)
work they are assigned
they will need to be linked to all other employees unnder them
Basically :(Employer Object)->(like 10 Employee)
they can modify the work assigned to employees and manage priority levels.

request
Basically to allow managers to see and hear the employees,increasing satisfaction
prolly just an id that is linked to the employee and then the actual request itself


?Work
Basically rather than storing the work in each object why not have a separate table linked to each employee (i dont know if its right)
If made: i would need a list of work and each employee would be linked using their employee id or name /names cant be sued as the main because they maybe same but i need to add the way for managers to search by name
we can also allow the leader to make changes right like if the work is done how much is pending and reassign it

-----------------------------------------------------------------------------------------------------------------------------------
Services (basically the business logic , All  business roles in services only)
Admin service or something like that to seed leaders

One simple employer service to allow the employer to create a new profile for recruits.
They can update the profile of an existing employee to add which work they have to do and its priority value.
They can whether or not the work is done if not then reassign it or if done mark it such.
They can check a particular employee and see how much is done

Employee service to handle creation of employees
handle whether work is done.A work item cannot be marked “in progress” or “completed” if any of its dependencies are still incomplete basically ythink of a linked list
if not then how much is left.
time logged on a day
any requests they wanna send ahead(Limit it to 5 anytime from one employee to protect abuse)
-----------------------------------------------------------------------------------------------------------------------------------

Controllers 

!!!!!MUST i want to have a endpoint to test if everything is up and rnunning debugging becomes easier,i dont know how to design it
GET /health -> system status

Manager controller(/manager)
One will be GET /{$employeeID} -> allows the managers to fetch the data of a given employee to see if they're done or not and maybe rate in the future.
POST /create ->allows managers to make a new employee fill in their details
PUT /assign->allows them to assign work to employee
DELETE /deactivate->basically someway to deactivate an employee 
PATCH /reassign ->Allows work to be shifted

Employee controller(/employee/${employeeID})
It will have
GET /details-> allow them to see their own profile
GET /work-> allow them to see the work assigned to them
PATCH some endpoint to update the status of the work and if they're done +Hours logged

-----------------------------------------------------------------------------------------------------------------------------------
Security
SecurityConfig-> Handles the cors and stuff.Handles passwordEncoder() handles SecurityyFilerChain,authenticationManager.
JwtAuthenticationEntryPoint->authenticationentrypoint
Users can only access resources they own or are authorized to manage, enforced at the API boundary.

NOTES:manager and employer have been used interchangeably

-----------------------------------------------------------------------------------------------------------------------------------


Services required

