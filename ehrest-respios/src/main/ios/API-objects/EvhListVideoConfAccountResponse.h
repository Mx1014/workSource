//
// EvhListVideoConfAccountResponse.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhVideoConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVideoConfAccountResponse
//
@interface EvhListVideoConfAccountResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhVideoConfAccountDTO*
@property(nonatomic, strong) NSMutableArray* videoConfAccounts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

