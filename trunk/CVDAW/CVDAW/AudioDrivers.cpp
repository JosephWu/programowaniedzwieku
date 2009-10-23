#include "StdAfx.h"
#include "AudioDrivers.h"
#include "inc\fmod.hpp"



AudioDrivers::AudioDrivers(void)
{
	FMOD_RESULT result;
	FMOD::System *system;
	FMOD::Channel    *channel = 0;

	result = FMOD::System_Create(&system);		// Create the main system object.
	if (result != FMOD_OK)
	{
		printf("FMOD error! (%d) %s\n", result, FMOD_ErrorString(result));
		std::cout<<FMOD_ErrorString(result);
		exit(-1);
	}

	result = system->init(100, FMOD_INIT_NORMAL, 0);	// Initialize FMOD.
	if (result != FMOD_OK)
	{
		printf("FMOD error! (%d) %s\n", result, FMOD_ErrorString(result));
		std::cout<<FMOD_ErrorString(result);
		exit(-1);
	}

	FMOD::Sound *sound;
	result = system->createSound("../media/test.mp3", FMOD_DEFAULT, 0, &sound);		// FMOD_DEFAULT uses the defaults.  These are the same as FMOD_LOOP_OFF | FMOD_2D | FMOD_HARDWARE.
	ERRCHECK(result);
	result = system->playSound(FMOD_CHANNEL_FREE, sound, false, &channel);
    ERRCHECK(result);
}



void AudioDrivers::ERRCHECK(FMOD_RESULT result) {
	if (result != FMOD_OK)
	{
		std::cout<<"FMOD error! (%d) %s\n";
		exit(-1);
	}
};