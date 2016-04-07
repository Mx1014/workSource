//
// EvhFamilyListOwningFamilyMembersRestResponse.h
// generated at 2016-04-07 15:16:54 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyListOwningFamilyMembersRestResponse
//
@interface EvhFamilyListOwningFamilyMembersRestResponse : EvhRestResponseBase

// array of EvhFamilyMemberDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
