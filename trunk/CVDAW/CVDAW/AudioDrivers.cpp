#include "StdAfx.h"
#include "AudioDrivers.h"
#include "inc\fmod.hpp"
#include "Form1.h"

AudioDrivers::AudioDrivers()
{	
};


AudioDrivers::AudioDrivers(Form1* form1)
{	
	this->form1 = form1;
	FMOD::Channel    *channel = 0;

	result = FMOD::System_Create(&this->system);
	this->ERRCHECK(result);

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
};


void AudioDrivers::getPlugins(FMOD_PLUGINTYPE pluginType) {
	StringBuildier toRet;

	int numberOfPlugins;
	char name[256];

	result = system->getNumPlugins(pluginType, &numberOfPlugins);
	ERRCHECK(result);
	for (int i = 0; i < num; i++)
	{
		result = system->getPluginHandle(pluginType, i, &handle);
		ERRCHECK(result);

		result = system->getPluginInfo(handle, 0, name, 256, 0);
		ERRCHECK(result);

	}
}


void AudioDrivers::playSound() {
}

void AudioDrivers::setOutputByPlugin(int num) {
    this->result = system->getPluginHandle(FMOD_PLUGINTYPE_OUTPUT, num, &this->handle);
    ERRCHECK(this->result);
    result = system->setOutputByPlugin(this->handle);
    ERRCHECK(this->result);
}



void AudioDrivers::ERRCHECK(FMOD_RESULT result) {
	if (this->result != FMOD_OK)
	{
		std::cout<<"FMOD error!\n";
		exit(-1);
	}
}