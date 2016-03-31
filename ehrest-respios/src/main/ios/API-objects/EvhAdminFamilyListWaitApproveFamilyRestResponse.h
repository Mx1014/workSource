//
// EvhAdminFamilyListWaitApproveFamilyRestResponse.h
// generated at 2016-03-31 11:07:27 
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
