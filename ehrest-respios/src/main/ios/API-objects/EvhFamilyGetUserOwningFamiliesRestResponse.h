//
// EvhFamilyGetUserOwningFamiliesRestResponse.h
// generated at 2016-04-19 13:40:01 
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
