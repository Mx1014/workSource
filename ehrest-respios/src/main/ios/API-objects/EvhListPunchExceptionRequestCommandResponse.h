//
// EvhListPunchExceptionRequestCommandResponse.h
// generated at 2016-03-25 19:05:20 
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

