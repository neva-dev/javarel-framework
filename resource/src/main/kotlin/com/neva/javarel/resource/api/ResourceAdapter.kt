package com.neva.javarel.resource.api

import com.neva.javarel.foundation.adapting.Adapter

interface ResourceAdapter<Target : Any> : Adapter<Resource, Target>