//
// EvhFamilyListFamilyMembersByFamilyIdRestResponse.h
// generated at 2016-04-19 14:25:58 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyListFamilyMembersByFamilyIdRestResponse
//
@interface EvhFamilyListFamilyMembersByFamilyIdRestResponse : EvhRestResponseBase

// array of EvhFamilyMemberDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
