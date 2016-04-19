//
// EvhAdminFamilyListWaitApproveFamilyRestResponse.h
// generated at 2016-04-19 12:41:54 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminFamilyListWaitApproveFamilyRestResponse
//
@interface EvhAdminFamilyListWaitApproveFamilyRestResponse : EvhRestResponseBase

// array of EvhListWaitApproveFamilyCommandResponse* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
