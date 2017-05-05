# ACL-Aid
Access Control List (ACL) authorization helper (aid) library. Pronounced 'accolade'.

After a user has been identified as being authentic (via authentication), an application usually provides resources to that user. However, applications usually have different access rules for various resource types. This process of determining who has access to which resources is called authorization. Authorization in its simplest form is the composition of these elements:

1. the user, system, etc. desiring access (herein after called 'role')
2. the resource the principal is wanting access to (this is the 'resource')
3. and usually, the operation that is being performed on the resource (often called a 'permission' or a 'privilege')

In other words, authorization answers the question: "Does this role have this permission on this resource?"

One way to implement authorization rules is via an Access Control List (ACL) which specifies all the permissions each principal has on each resource. This can be very detailed, repetitive and tedious to specify all the rules for each user. One variation is called Role-Based Access Control (RBAC), which specifies resource permissions for a 'role'--then assign each user to one or more roles. This approach assigns permissions to principals based on the role (or roles) they play instead of specifying individual permissions for each principle. This is the way ACL-Aid works--with roles, resources, and permissions.

For our purposes, role, resource and permission are simply strings. However, ACL-Aid also provides 3 corresponding interfaces: Role, Resource, Permission. This enables us to use both string constants and dynamic values in objects to perform authorization.

## Role

It's fair to think of a role as a group of people or a function played by a person or thing. Some example roles are: user, group, owner, system, administrator. A role in ACL-Aid is simply a label that can be applied to a user or security principal.

Any Java object can play a role by simply implementing the *Role* interface and implementing the getRoleId() method, which simply returns a String.

Below is a simple user model, which takes part in our authorization system by implementing the *Role* interface. The method getRoleId() will return the id "guest" when an ID is not known, or it will return the role ID that was assigned to this actual user object. This value can effectively come from anywhere, a static definition or perhaps dynamically from the users database role itself.

``` Java
public class User
implements Role
{
	private String roleId = null;

	public String getRoleId()
	{
		return (roleId == null ? "guest" : roleId);
	}
}
```

## Permission

Any Java object can become a permission by simply implementing the *Permission* interface and implementing the getPermissionId() method, which simply returns a String.

``` Java
public class View
implements Permission
{
	public String getPermissionId()
	{
		return "view";
	}
}

public class Delete
implements Permission
{
	public String getPermissionId()
	{
		return "delete";
	}
}

```

## Resource

Any Java object can become a resource by simply implementing the *Resource* interface and implementing the getResourceId() method, which simply returns a String.

``` Java
public class BlogPost
implements Resource
{
	public String getResourceId()
	{
		return "blogPost";
	}
}
```

# Defining Access Rules

Now that we have defined a role, a resource, and permissions, we can define the the rules for the ACL. These rules are consulted when the application needs to know what is possible given a certain role, resource, and permission.

Let's define the following simple rules:

```java
AccessControlList acl = new AccessControlList();

// setup the various roles in our system
acl.addRole("guest");

// owner inherits all of the rules of guest
acl.addRole("owner", "guest");
 
// add the resources
acl.addResource("blogPost");
 
// add privileges to roles and resource combinations
acl.allow("guest", "blogPost", "view");
acl.allow("owner", "blogPost", "delete");
```

The above rules are quite simple: a guest role and an owner role exist; as does a blogPost type resource. A guests is allowed to view blog posts, and owners are allowed to view and post blog posts.

## Wildcard Access Rules

ACL-Aid supports the notion of "wildcard" access rules, where you can, if desired use a wildcard for the resource ID or the permission ID when defining access rules. Suppose your system has few enough permissions and resources in it that you don't need the complexity of specifying the resources separately. In this case, your permissions could have the resource name in them. And then you can specify the access rules without the resource ID as follows:

```java
AccessControlList acl = new AccessControlList();

// setup the various roles in our system
acl.addRole("guest");
 
// NOTE: no resource registrations needed

// owner inherits all of the rules of guest
acl.addRole("owner", "guest");
 
// add privileges to roles (without resources)
acl.allow("guest", "view_post");
acl.allow("owner", "delete_post");
```

## Conditions

What if we want to ensure a specific owner actually owns a specific blog post before allowing it do be deleted? In other words, we want to ensure that only post owners have the ability to delete their own posts.

This is where conditions come in. Conditions are methods that will be called when the static rule checking is simply not enough. A condition object is consulted to dynamically determine if the role has the required permission on the resource. This is a way to get dynamic logic into an ACL.

For this example, we'll use the following condition:

```java
public class BlogPostCondition
implements Condition
{
	public boolean evaluate(String roleId, Resource resource, String permissionId)
	{
	}
}
```

# Querying Access Rules

In its simplest form, you could use role-checking guard clauses in your application like this:

```java
if ("guest".equals(user.getRoleId))
{
	// do guest logic here
}
else
{
	// do other role logic here.
}
```

However, that is not very dynamic and is subject to change over time, especially in the case of a complex multi-tenant application. So instead, we want to assign permissions to roles to provide fine-grained access rules while separating out authorization logic from our domain model.

## Using the Annotations

```java
@RequiresPermission(value = "view", resource = "blogPost")
```
Or, if using wildcard resources:

```java
@RequiresPermission("view_post")
```
