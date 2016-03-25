//
// EvhListBusinessesByKeywordAdminCommandResponse.h
// generated at 2016-03-25 15:57:22 
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

