package com.github.ogapants.datastateview;

public enum LoadState {
    LOADING, //data is being loaded...
    LOADED_EMPTY,//data list is loaded, but content of list data is empty
    ERROR,  //data is cannot loaded
    DISABLE //data loading has not started or has been already completed

// I'm thinking about these names... 🤔
// Completed
// Error
// InProgress
// NotStarted
// UNLOADED
// NOT_LOADED
// SUCCESS
// NotSet
// Set
// disable
// hide
// STUB
}
