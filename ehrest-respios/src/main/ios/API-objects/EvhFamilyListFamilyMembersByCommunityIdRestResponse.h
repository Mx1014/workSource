//
// EvhFamilyListFamilyMembersByCommunityIdRestResponse.h
// generated at 2016-04-07 10:47:33 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyListFamilyMembersByCommunityIdRestResponse
//
@interface EvhFamilyListFamilyMembersByCommunityIdRestResponse : EvhRestResponseBase

// array of EvhFamilyMemberDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
