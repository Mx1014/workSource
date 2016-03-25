//
// EvhListGroupContactsCommandResponse.h
// generated at 2016-03-25 15:57:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhGroupContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListGroupContactsCommandResponse
//
@interface EvhListGroupContactsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhGroupContactDTO*
@property(nonatomic, strong) NSMutableArray* list;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

