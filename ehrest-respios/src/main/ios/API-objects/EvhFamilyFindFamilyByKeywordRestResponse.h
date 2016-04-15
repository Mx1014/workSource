//
// EvhFamilyFindFamilyByKeywordRestResponse.h
// generated at 2016-04-12 15:02:21 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyFindFamilyByKeywordRestResponse
//
@interface EvhFamilyFindFamilyByKeywordRestResponse : EvhRestResponseBase

// array of EvhFamilyDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
