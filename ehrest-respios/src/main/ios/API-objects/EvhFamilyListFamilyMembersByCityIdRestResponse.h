//
// EvhFamilyListFamilyMembersByCityIdRestResponse.h
// generated at 2016-03-31 10:18:21 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyListFamilyMembersByCityIdRestResponse
//
@interface EvhFamilyListFamilyMembersByCityIdRestResponse : EvhRestResponseBase

// array of EvhFamilyMemberDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
