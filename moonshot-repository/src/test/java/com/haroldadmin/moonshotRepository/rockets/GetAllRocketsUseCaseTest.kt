package com.haroldadmin.moonshotRepository.rockets

/**
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.database.rocket.RocketsDao
import com.haroldadmin.moonshot.models.rocket.RocketMinimal
import com.haroldadmin.moonshotRepository.FakeDataProvider
import com.haroldadmin.moonshotRepository.rocket.GetAllRocketsUseCase
import com.haroldadmin.moonshotRepository.rocket.PersistRocketsUseCase
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetAllRocketsUseCaseTest : DescribeSpec({

    describe("Fetching all rockets") {

        context("Cache is invalid") {
            val apiRockets = FakeDataProvider.getApiRockets(1)

            val mockDao = mockk<RocketsDao> {
                coEvery { getAllRockets() } returns listOf()
                coEvery { saveRocketsWithPayloadWeights(any(), any()) } returns Unit
            }
            val mockService = mockk<RocketsService> {
                every {
                    getAllRockets()
                } returns CompletableDeferred(NetworkResponse.Success(apiRockets))
            }

            val mockPersistUseCase = mockk<PersistRocketsUseCase> {
                coEvery { persistApiRockets(any()) } returns Unit
            }

            val usecase = GetAllRocketsUseCase(mockDao, mockService, mockPersistUseCase)

            it("Should eventually return refreshed data") {
                val resource = usecase.getAllRockets().last()
                with(resource) {
                    shouldBeTypeOf<Resource.Success<List<RocketMinimal>>>()
                    this as Resource.Success
                    isCached shouldBe false
                }
            }
        }
    }
})
        **/