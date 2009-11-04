#pragma once
#include "inc\fmod.hpp"
#include "inc\fmod_errors.h"
#include <iostream>
#include "Form1.h"

ref class AudioDrivers
{
public:
	FMOD::System *system;
	FMOD_RESULT result;
	unsigned int handle;
	Form1 form1;
	AudioDrivers();
	AudioDrivers(Form1 *form1);
	void AudioDrivers::setOutputByPlugin(int num);
	void AudioDrivers::playSound();
	void AudioDrivers::getPlugins(FMOD_PLUGINTYPE pluginType);
private: 
	void ERRCHECK(FMOD_RESULT result);
};
