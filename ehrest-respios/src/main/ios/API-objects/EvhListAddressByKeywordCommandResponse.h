//
// EvhListAddressByKeywordCommandResponse.h
// generated at 2016-03-25 17:08:11 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAddressByKeywordCommandResponse
//
@interface EvhListAddressByKeywordCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhAddressDTO*
@property(nonatomic, strong) NSMutableArray* requests;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

