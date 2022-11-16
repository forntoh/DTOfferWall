package dev.forntoh.web_service_test.api

import dev.forntoh.web_service.api.ApiManager
import dev.forntoh.web_service.api.MainApiService

class ApiManagerImplTest(
    override val mainApi: MainApiService
) : ApiManager