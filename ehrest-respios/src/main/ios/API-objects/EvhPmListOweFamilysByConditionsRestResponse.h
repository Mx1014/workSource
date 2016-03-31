//
// EvhPmListOweFamilysByConditionsRestResponse.h
// generated at 2016-03-31 11:07:27 
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
