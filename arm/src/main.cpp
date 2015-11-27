//
// This file is part of the GNU ARM Eclipse distribution.
// Copyright (c) 2014 Liviu Ionescu.
//

// ----------------------------------------------------------------------------

#include <stdio.h>
#include <stdlib.h>
#include "diag/Trace.h"

#include "Timer.h"
#include "FreeRTOS.h"
#include "task.h"
#include "Uart.hpp"
#include "_initialize_hardware.c"

// ----- main() ---------------------------------------------------------------

// Sample pragmas to cope with warnings. Please note the related line at
// the end of this function, used to pop the compiler diagnostics status.
#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"
#pragma GCC diagnostic ignored "-Wmissing-declarations"
#pragma GCC diagnostic ignored "-Wreturn-type"

TIM_Encoder_InitTypeDef encoder;
TIM_HandleTypeDef timer;
Uart<2> serial_pc;

void hello_world_task(void* p)
{
	while(1)
	{
	  serial_pc.printfln("T3");
	  vTaskDelay(1000);
	}
}

void hello_world_task2(void* p)
{
	while(1)
	{
	  serial_pc.printfln("ABWABWA");
	  vTaskDelay(1000);
	}
}

int main(int argc, char* argv[])
{
	 HAL_Init();
	 SystemClock_Config();
	 // Test de codeur
/*
	 HAL_NVIC_SetPriorityGrouping(NVIC_PRIORITYGROUP_4);
	 HAL_NVIC_SetPriority(SysTick_IRQn, 0, 1);

	 timer.Instance = TIM3;
	 timer.Init.Period = 0xFFFF;
	 timer.Init.CounterMode = TIM_COUNTERMODE_UP;
	 timer.Init.Prescaler = 0;
	 timer.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;

	 HAL_TIM_Encoder_MspInit(&timer);

	 encoder.EncoderMode = TIM_ENCODERMODE_TI12;

	 encoder.IC1Filter = 0x0F;
	 encoder.IC1Polarity = TIM_INPUTCHANNELPOLARITY_RISING;
	 encoder.IC1Prescaler = TIM_ICPSC_DIV4;
	 encoder.IC1Selection = TIM_ICSELECTION_DIRECTTI;

	 encoder.IC2Filter = 0x0F;
	 encoder.IC2Polarity = TIM_INPUTCHANNELPOLARITY_FALLING;
	 encoder.IC2Prescaler = TIM_ICPSC_DIV4;
	 encoder.IC2Selection = TIM_ICSELECTION_DIRECTTI;

	 if (HAL_TIM_Encoder_Init(&timer, &encoder) != HAL_OK) {
		 // TODO
	 }

	 if(HAL_TIM_Encoder_Start_IT(&timer,TIM_CHANNEL_1)!=HAL_OK){
		 // TODO
	 }*/

//  Timer timer2;
  serial_pc.init(9600);
 // timer2.start ();
  char out[50];
//  osKernelInitialize();
  xTaskCreate(hello_world_task, (char*)"TEST1", (2048)/4, 0, 1, 0);
  xTaskCreate(hello_world_task2, (char*)"TEST2", (2048)/4, 0, 1, 0);
  vTaskStartScheduler();
  while(1)
  {
	  serial_pc.printfln("ERREUR");
//	  serial_pc.printfln("%d", TIM3->CNT);
  }

  return 0;
}


void HAL_TIM_Encoder_MspInit(TIM_HandleTypeDef *htim)
	{
 GPIO_InitTypeDef GPIO_InitStruct;

 if (htim->Instance == TIM3) {

 __TIM3_CLK_ENABLE();

 __GPIOB_CLK_ENABLE();

 GPIO_InitStruct.Pin = GPIO_PIN_4 | GPIO_PIN_5;
 GPIO_InitStruct.Mode = GPIO_MODE_AF_PP;
 GPIO_InitStruct.Pull = GPIO_PULLUP;
 GPIO_InitStruct.Speed = GPIO_SPEED_HIGH;
 GPIO_InitStruct.Alternate = GPIO_AF2_TIM3;
 HAL_GPIO_Init(GPIOB, &GPIO_InitStruct);

 HAL_NVIC_SetPriority(TIM3_IRQn, 0, 1);

 HAL_NVIC_EnableIRQ(TIM3_IRQn);
 }
}

void TIM3_IRQHandler(void){
 HAL_TIM_IRQHandler(&timer);
}
#pragma GCC diagnostic pop

// ----------------------------------------------------------------------------
