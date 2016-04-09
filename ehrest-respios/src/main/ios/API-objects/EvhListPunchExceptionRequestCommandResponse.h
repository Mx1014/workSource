//
// EvhListPunchExceptionRequestCommandResponse.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPunchExceptionRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPunchExceptionRequestCommandResponse
//
@interface EvhListPunchExceptionRequestCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhPunchExceptionRequestDTO*
@property(nonatomic, strong) NSMutableArray* exceptionRequestList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

