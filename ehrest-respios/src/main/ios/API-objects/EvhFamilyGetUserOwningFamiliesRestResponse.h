//
// EvhFamilyGetUserOwningFamiliesRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyGetUserOwningFamiliesRestResponse
//
@interface EvhFamilyGetUserOwningFamiliesRestResponse : EvhRestResponseBase

// array of EvhFamilyDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
