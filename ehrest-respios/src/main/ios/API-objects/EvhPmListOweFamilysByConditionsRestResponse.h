//
// EvhPmListOweFamilysByConditionsRestResponse.h
// generated at 2016-04-07 14:16:31 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListOweFamilysByConditionsRestResponse
//
@interface EvhPmListOweFamilysByConditionsRestResponse : EvhRestResponseBase

// array of EvhOweFamilyDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
