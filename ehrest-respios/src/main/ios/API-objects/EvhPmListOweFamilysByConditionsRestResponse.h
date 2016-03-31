//
// EvhPmListOweFamilysByConditionsRestResponse.h
// generated at 2016-03-31 13:49:15 
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
