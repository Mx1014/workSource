//
// EvhListBusinessesByKeywordAdminCommandResponse.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBusinessAdminDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListBusinessesByKeywordAdminCommandResponse
//
@interface EvhListBusinessesByKeywordAdminCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhBusinessAdminDTO*
@property(nonatomic, strong) NSMutableArray* requests;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

