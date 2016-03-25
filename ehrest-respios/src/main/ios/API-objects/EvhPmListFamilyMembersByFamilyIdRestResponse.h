//
// EvhPmListFamilyMembersByFamilyIdRestResponse.h
// generated at 2016-03-25 11:43:35 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListFamilyMembersByFamilyIdRestResponse
//
@interface EvhPmListFamilyMembersByFamilyIdRestResponse : EvhRestResponseBase

// array of EvhFamilyMemberDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
