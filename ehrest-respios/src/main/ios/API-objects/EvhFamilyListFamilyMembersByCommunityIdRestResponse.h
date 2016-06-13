//
// EvhFamilyListFamilyMembersByCommunityIdRestResponse.h
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
