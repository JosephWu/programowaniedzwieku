#pragma once
#include "inc\fmod.hpp"
#include "inc\fmod_errors.h"
#include <iostream>

ref class AudioDrivers
{
public:
	AudioDrivers(void);
private: 
	void ERRCHECK(FMOD_RESULT result);
};
