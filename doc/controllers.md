# Controllers

Javarel introduces JAX-RS support using Jersey implementation. To configure it, we have to simply create various types of REST components.

## Resources

Assume that we want to create an entry point to our application for accessing data related with user. So let's create a user controller (resource) which will be a simple example of REST component.

    @Component
    @Service
    @Path("/user")
    class UserController : RestComponent {
    
        @Reference
        private lateinit var userRepository: UserRepository
    
        @Reference
        private lateinit var resourceResolver: ResourceResolver
    
        @GET
        @Path("/show/{id}")
        fun getShow(id: String): Response {
            val user = userRepository.findOrFail(id)
            val html = resourceResolver.findOrFail("bundle://app/view/user/show.peb")
                    .adaptTo(View::class)
                    .with("user", user)
                    .render()
    
            return Response.ok(html)
                    .type(MediaType.TEXT_HTML)
                    .build()
        }
    
    }

## Error handlers

Not only controller could be a REST component. Implement a component which inherits from exception mapper to create an error handler.

    @Component
    @Service(RestComponent::class)
    @Provider
    class ThrowableMapper : ExceptionMapper<Throwable>, RestComponent {
    
        override fun toResponse(e: Throwable): Response {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Error occurred! ${e.message}")
                            .type(MediaType.TEXT_HTML)
                            .build()
        }
        
    }
    
    